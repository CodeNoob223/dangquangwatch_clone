package edu.it10.dangquangwatch.spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import edu.it10.dangquangwatch.spring.AppCustomException.SaveAccountException;
import edu.it10.dangquangwatch.spring.AppCustomException.EmptyOrNullListException;
import edu.it10.dangquangwatch.spring.AppCustomException.ErrorEnum;
import edu.it10.dangquangwatch.spring.entity.DonHang;
import edu.it10.dangquangwatch.spring.entity.TaiKhoan;
import edu.it10.dangquangwatch.spring.entity.enumeration.OrderStatus;
import edu.it10.dangquangwatch.spring.entity.request.CheckoutRequest;
import edu.it10.dangquangwatch.spring.entity.response.ApiResponse;
import edu.it10.dangquangwatch.spring.entity.response.ObjectResponse;
import edu.it10.dangquangwatch.spring.payment.CardInfo;
import edu.it10.dangquangwatch.spring.payment.GlobalCardInfo;
import edu.it10.dangquangwatch.spring.payment.LocalCardInfo;
import edu.it10.dangquangwatch.spring.service.ChiTietDonHangService;
import edu.it10.dangquangwatch.spring.service.DonHangService;
import edu.it10.dangquangwatch.spring.service.OtpService;
import edu.it10.dangquangwatch.spring.service.TaiKhoanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {
  @Autowired
  DonHangService donHangService;
  @Autowired
  ChiTietDonHangService ctdhService;
  @Autowired
  TaiKhoanService taiKhoanService;
  @Autowired
  OtpService otpService;

  @GetMapping("/")
  public String index() {
    return "redirect:/profile/giohang";
  }

  @GetMapping("/giohang")
  public String cart(HttpSession session, Model model) {
    var username = session.getAttribute("username");

    if (username != null) {
      TaiKhoan taikhoan = taiKhoanService.getTaiKhoan((String) username);

      model.addAttribute("taikhoan", taikhoan);
      return "giohang";
    } else {
      return "redirect:/";
    }
  }

  @GetMapping("/donhang")
  public String orders(HttpSession session,
      @RequestParam("search") Optional<String> search,
      @RequestParam("tinhtrang") Optional<String> tinhtrang,
      @RequestParam("thanhtoan") Optional<String> thanhtoan,
      @RequestParam("from") Optional<String> from,
      @RequestParam("to") Optional<String> to,
      @RequestParam("page") Optional<Integer> page, Model model) {
    String fromStr = "2001-01-01";
    String toStr = "3000-01-01";
    if (from.isPresent()) {
      if (!from.get().isEmpty())
        fromStr = from.get();
    }
    if (to.isPresent()) {
      if (!to.get().isEmpty())
        toStr = to.get();
    }

    List<String> tinhtrang_options = new ArrayList<String>();
    tinhtrang_options.add("Đã hủy");
    tinhtrang_options.add("Chờ xác nhận");
    tinhtrang_options.add("Đã xác nhận");
    tinhtrang_options.add("Đang vận chuyển");
    tinhtrang_options.add("Đã nhận hàng");

    List<String> thanhtoan_options = new ArrayList<String>();
    thanhtoan_options.add("Chưa thanh toán");
    thanhtoan_options.add("Khi nhận hàng");
    thanhtoan_options.add("Đã thanh toán");
    thanhtoan_options.add("Đã hoàn tiền");
    thanhtoan_options.add("Đã nhận hàng");

    String tinhtrangStr = "";
    if (tinhtrang.isPresent())
      tinhtrangStr = tinhtrang.get();
    String thanhtoanStr = "";
    if (thanhtoan.isPresent())
      thanhtoanStr = thanhtoan.get();
    int pageNum = 0;
    if (page.isPresent())
      pageNum = page.get() - 1;

    String searchStr = "";
    if (search.isPresent())
      searchStr = search.get().trim();

    String username = (String) session.getAttribute("username");

    Page<DonHang> data = donHangService.getMyDonHang(searchStr, tinhtrangStr, thanhtoanStr, username, fromStr, toStr,
        pageNum);

    model.addAttribute("donhangs", data.getContent());
    model.addAttribute("sotrang", data.getTotalPages());
    model.addAttribute("search", searchStr);
    model.addAttribute("tinhtrang", tinhtrangStr);
    model.addAttribute("thanhtoan", thanhtoanStr);
    model.addAttribute("tinhtrang_options", tinhtrang_options);
    model.addAttribute("thanhtoan_options", thanhtoan_options);
    model.addAttribute("from", from.isPresent() ? from.get() : "");
    model.addAttribute("to", to.isPresent() ? to.get() : "");
    model.addAttribute("page", pageNum);
    return "donhang";
  }

  @ExceptionHandler(EmptyOrNullListException.class)
  public ResponseEntity<String> handleEmptyOrNullListException(EmptyOrNullListException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @PostMapping("/kiemtradonhang")
  public ResponseEntity<ObjectResponse<DonHang>> toCheckOut(
      HttpSession session,
      Model model,
      @RequestBody DonHang donHang) {
    String username = (String) session.getAttribute("username");
    TaiKhoan user = taiKhoanService.getTaiKhoan(username);

    TaiKhoan tempTaiKhoan = new TaiKhoan();
    tempTaiKhoan.setUsername(username);
    tempTaiKhoan.setDiachi(user.getDiachi());
    tempTaiKhoan.setSodienthoai(user.getSodienthoai());
    tempTaiKhoan.setHoten(user.getHoten());
    tempTaiKhoan.setLoai_tai_khoan(user.getLoai_tai_khoan());

    // Check don hang valid
    donHang = donHangService.validate(donHang, tempTaiKhoan);

    return ResponseEntity.ok(new ObjectResponse<DonHang>(true, "Đơn hàng hợp lệ!", donHang));
  }

  @PostMapping("/checkout")
  public ResponseEntity<ApiResponse> placeOrder(
      @Valid @RequestBody CheckoutRequest request) {

    DonHang donHang = request.getDonHang();

    if (request.getCardInfo() != null) {
      CardInfo info = request.getCardInfo();

      ApiResponse response = null;
      if (info instanceof GlobalCardInfo) {
        response = donHangService.checkOutDonHang(donHang, (GlobalCardInfo) info);
      } else if (info instanceof LocalCardInfo) {
        response = donHangService.checkOutDonHang(donHang, (LocalCardInfo) info);
      }

      return ResponseEntity.ok(response);
    } else {
      ApiResponse response = donHangService.checkOutDonHang(donHang);

      return ResponseEntity.ok(response);
    }
  }

  @PostMapping("/huydon")
  public String cancelOrder(HttpServletRequest request, @RequestParam("madonhang") Integer madonHang) {
    HttpSession session = request.getSession(false);
    String currentUserId = (String) session.getAttribute("username");

    Optional<DonHang> data = donHangService.findDonHangById(madonHang);
    TaiKhoan currentUser = taiKhoanService.getTaiKhoan(currentUserId);

    if (data.isPresent()) {
      DonHang donHang = data.get();
      if (donHang.getTaikhoan().getUsername() == currentUser.getUsername() || currentUser.isAdmin()) {
        donHangService.updateStatus(madonHang, OrderStatus.CANCELLED);
      }
    }

    return "redirect:/profile/donhang";
  }

  @GetMapping("/doithongtin")
  public String doithongtin(HttpSession session, Model model) {
    var username = session.getAttribute("username");

    if (username == null) {
      return "redirect:/";
    }

    var sessionErr = session.getAttribute(ErrorEnum.UPDATE_PROFILE_ERROR.name());
    if (sessionErr != null) {
      model.addAttribute("errorMessage", (String) sessionErr);
      session.removeAttribute(ErrorEnum.UPDATE_PROFILE_ERROR.name());
    }

    TaiKhoan taikhoan = taiKhoanService.getTaiKhoan((String) username);

    model.addAttribute("taikhoan", taikhoan);

    String address = taikhoan.getDiachi();
    String province = "";
    String district = "";
    String ward = "";
    String extra = "";

    if (taikhoan.getDiachi() != null && !taikhoan.getDiachi().equals("Chưa có")) {
      String[] addressSplit = address.split(", ", 4);
      if (addressSplit.length == 4) {
        province = addressSplit[0];
        district = addressSplit[1];
        ward = addressSplit[2];
        extra = addressSplit[3];
      }
    }

    model.addAttribute("province", province);
    model.addAttribute("district", district);
    model.addAttribute("ward", ward);
    model.addAttribute("extra", extra);
    return "doithongtin";
  }

  @PostMapping("/doithongtin")
  public String updateThongTin(TaiKhoan taiKhoan, HttpSession session) {
    String taikhoanUsername = (String) session.getAttribute("username");
    TaiKhoan existingTaiKhoan = taiKhoanService.getTaiKhoan(taikhoanUsername);

    if (taiKhoan.getPassword() == null) {
      taiKhoan.setPassword(existingTaiKhoan.getPassword());
    }

    try {
      taiKhoanService.updateTaiKhoan(taiKhoan, "/profile/doithongtin");
    } catch (SaveAccountException e) {
      e.printStackTrace();
      session.setAttribute(ErrorEnum.UPDATE_PROFILE_ERROR.name(), e.getMessage());
      return "redirect:/profile/doithongtin";
    }
    return "redirect:/profile/doithongtin";
  }

  @PostMapping("/doimatkhau")
  public String doimatkhau(
      @RequestParam("newpassword") String newpassword,
      @RequestParam("username") String username,
      RedirectAttributes redirectAttributes) {

    otpService.createChangePasswordUrl(username, newpassword);
    // Thêm thông báo vào RedirectAttributes
    redirectAttributes.addFlashAttribute("notification", "Vui lòng xác thực mật khẩu mới trong email!");
    return "redirect:/profile/doithongtin";
  }

  @PostMapping("/doisodienthoai")
  public String doisodienthoai(
      @RequestParam("newphonenumber") String newphonenumber,
      @RequestParam("username") String username,
      RedirectAttributes redirectAttributes) {

    otpService.createVerifyPhoneNumberCode(username, newphonenumber);
    // Thêm thông báo vào RedirectAttributes
    redirectAttributes.addFlashAttribute("newphonenumber", newphonenumber);
    return "redirect:/profile/phoneotp";
  }

  @GetMapping("/phoneotp")
  public String phoneotp(HttpSession session, Model model) {
    var username = session.getAttribute("username");

    if (username == null) {
      return "redirect:/login";
    }

    if (!model.containsAttribute("newphonenumber")) {
      return "redirect:/profile/doithongtin";
    }
    model.addAttribute("username", (String) username);
    return "phoneotp";
  }
}
