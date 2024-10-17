package edu.it10.dangquangwatch.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.it10.dangquangwatch.spring.AppCustomException.ErrorEnum;
import edu.it10.dangquangwatch.spring.entity.TaiKhoan;
import edu.it10.dangquangwatch.spring.service.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class DangkyController {
  @Autowired
  TaiKhoanService taiKhoanService;

  @GetMapping("/register")
  public String redirectToLogin() {
    return "redirect:/dangky";
  }

  @GetMapping("/dangky")
  public String register(HttpSession session, Model model) {
    String errorMessage = null;
    String province = "";
    String district = "";
    String ward = "";
    String extra = "";

    if (session != null) {
      AuthenticationException ex = (AuthenticationException) session
          .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      if (ex != null) {
        errorMessage = ex.getMessage();
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      } else {
        var tempErrorMessage = session.getAttribute(ErrorEnum.REGISTER_ERROR.name());
        var tempTaiKhoan = session.getAttribute("taikhoan");
        if (tempErrorMessage != null) {
          errorMessage = (String) tempErrorMessage;
          session.removeAttribute(ErrorEnum.REGISTER_ERROR.name());

          if (tempTaiKhoan != null) {
            TaiKhoan taikhoan = (TaiKhoan) tempTaiKhoan;
            session.removeAttribute("taikhoan");
            String address = taikhoan.getDiachi();

            if (taikhoan.getDiachi() != null && !taikhoan.getDiachi().equals("Chưa có")) {
              String[] addressSplit = address.split(", ", 4);
              if (addressSplit.length == 4) {
                province = addressSplit[0];
                district = addressSplit[1];
                ward = addressSplit[2];
                extra = addressSplit[3];
              }

              model.addAttribute("email", taikhoan.getUsername());
              model.addAttribute("hoten", taikhoan.getHoten());
              model.addAttribute("sodienthoai", taikhoan.getSodienthoai());
              model.addAttribute("diachi", taikhoan.getDiachi());
            }
          }
        }
      }
    }

    model.addAttribute("province", province);
    model.addAttribute("district", district);
    model.addAttribute("ward", ward);
    model.addAttribute("extra", extra);
    model.addAttribute("errorMessage", errorMessage);
    return "dangky";
  }

  @GetMapping("/xacthuc")
  public String verifyAccount(
      HttpSession session,
      Model model,
      @RequestParam("email") Optional<String> email,
      @RequestParam("otp") Optional<String> otp) {

    if (!email.isPresent()) {
      session.setAttribute(ErrorEnum.REGISTER_ERROR.name(), "Link xác thực không hợp lệ!");
      return "redirect:/dangky";
    }

    if (!otp.isPresent()) {
      session.setAttribute(ErrorEnum.REGISTER_ERROR.name(), "Link xác thực không hợp lệ!");
      return "redirect:/dangky";
    }

    if (taiKhoanService.verifyOtp(otp.get(), email.get())) {
      model.addAttribute("notification", "Xác thực tài khoản thành công!");
      return "login";
    } else {
      model.addAttribute("errorMessage", "Link xác thực đã hết hạn hoặc không hợp lệ!");
      return "login";
    }
  }

  @PostMapping("/dangky")
  public String postDangky(HttpSession session, @RequestParam("username") String username,
      @RequestParam("password") String password, @RequestParam("diachi") String diachi,
      @RequestParam("hoten") String hoten, @RequestParam("sodienthoai") String sodienthoai,
      @RequestParam("agree") Optional<String> agree) {

    TaiKhoan taikhoan = new TaiKhoan();

    taikhoan.setUsername(username);
    taikhoan.setPassword(password);
    taikhoan.setSodienthoai(sodienthoai);
    taikhoan.setDiachi(diachi);
    taikhoan.setHoten(hoten);

    // Can xac thuc tai khoan qua email de duoc enable
    taikhoan.setEnabled(0);
    if (!agree.isPresent()) {
      session.setAttribute(ErrorEnum.REGISTER_ERROR.name(), "Bạn phải đồng ý điều khoản!");
      session.setAttribute("taikhoan", taikhoan);
      return "redirect:/dangky";
    }

    if (sodienthoai == null) {
      session.setAttribute(ErrorEnum.REGISTER_ERROR.name(), "Số điện thoại không hợp lệ!");
      session.setAttribute("taikhoan", taikhoan);
      return "redirect:/dangky";
    }

    if (diachi == null || diachi.isEmpty() || diachi.split(", ", 4).length < 4) {
      session.setAttribute(ErrorEnum.REGISTER_ERROR.name(), "Địa chỉ không hợp lệ!");
      session.setAttribute("taikhoan", taikhoan);
      return "redirect:/dangky";
    }

    taiKhoanService.dangKyKhachHang(taikhoan, "/dangky");

    return "redirect:/login?regsuccess";
  }
}
