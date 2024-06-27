package edu.it10.dangquangwatch.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "taikhoan")
@JsonIgnoreProperties({"donHangList"})
public class TaiKhoan {
  @Id
  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "hoten")
  private String hoten;

  @Column(name = "diachi")
  private String diachi;

  @Column(name = "loai_tai_khoan")
  private String loai_tai_khoan;

  @Column(name = "enabled")
  private String enabled;

  @OneToMany(mappedBy = "taikhoan")
  private List<DonHang> donHangList;

  public boolean isAdmin() {
    return loai_tai_khoan == "ROLE_QUANTRI";
  }

  public TaiKhoan() {
  }

  public TaiKhoan(String username, String password, String hoten, String diachi, String loai_tai_khoan, String enabled, List<DonHang> donHangList) {
    this.username = username;
    this.password = password;
    this.hoten = hoten;
    this.diachi = diachi;
    this.loai_tai_khoan = loai_tai_khoan;
    this.enabled = enabled;
    this.donHangList = donHangList;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHoten() {
    return this.hoten;
  }

  public void setHoten(String hoten) {
    this.hoten = hoten;
  }

  public String getDiachi() {
    return this.diachi;
  }

  public void setDiachi(String diachi) {
    this.diachi = diachi;
  }

  public String getLoai_tai_khoan() {
    return this.loai_tai_khoan;
  }

  public void setLoai_tai_khoan(String loai_tai_khoan) {
    this.loai_tai_khoan = loai_tai_khoan;
  }

  public String getEnabled() {
    return this.enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }

  public List<DonHang> getDonHangList() {
    return this.donHangList;
  }

  public void setDonHangList(List<DonHang> donHangList) {
    this.donHangList = donHangList;
  }

  public TaiKhoan username(String username) {
    setUsername(username);
    return this;
  }

  public TaiKhoan password(String password) {
    setPassword(password);
    return this;
  }

  public TaiKhoan hoten(String hoten) {
    setHoten(hoten);
    return this;
  }

  public TaiKhoan diachi(String diachi) {
    setDiachi(diachi);
    return this;
  }

  public TaiKhoan loai_tai_khoan(String loai_tai_khoan) {
    setLoai_tai_khoan(loai_tai_khoan);
    return this;
  }

  public TaiKhoan enabled(String enabled) {
    setEnabled(enabled);
    return this;
  }

  public TaiKhoan donHangList(List<DonHang> donHangList) {
    setDonHangList(donHangList);
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TaiKhoan)) {
            return false;
        }
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return Objects.equals(username, taiKhoan.username) && Objects.equals(password, taiKhoan.password) && Objects.equals(hoten, taiKhoan.hoten) && Objects.equals(diachi, taiKhoan.diachi) && Objects.equals(loai_tai_khoan, taiKhoan.loai_tai_khoan) && Objects.equals(enabled, taiKhoan.enabled) && Objects.equals(donHangList, taiKhoan.donHangList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, hoten, diachi, loai_tai_khoan, enabled, donHangList);
  }

  @Override
  public String toString() {
    return "{" +
      " username='" + getUsername() + "'" +
      ", password='" + getPassword() + "'" +
      ", hoten='" + getHoten() + "'" +
      ", diachi='" + getDiachi() + "'" +
      ", loai_tai_khoan='" + getLoai_tai_khoan() + "'" +
      ", enabled='" + getEnabled() + "'" +
      ", donHangList='" + getDonHangList() + "'" +
      "}";
  } 
}
