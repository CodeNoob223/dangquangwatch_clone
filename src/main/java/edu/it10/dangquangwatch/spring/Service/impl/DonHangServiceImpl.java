package edu.it10.dangquangwatch.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.it10.dangquangwatch.spring.entity.DonHang;
import edu.it10.dangquangwatch.spring.repository.DonHangRepository;
import edu.it10.dangquangwatch.spring.service.DonHangService;

@Service
public class DonHangServiceImpl implements DonHangService {
  @Autowired DonHangRepository donHangRepository;

  @Override
  public Page<DonHang> getAllDonHang(int page) {
    return donHangRepository.findAll(PageRequest.of(page, 10));
  }

  @Override
  public Page<DonHang> searchDonHang(String username, String diachi, String tensanpham, String tinhtrang, String thanhtoan, Integer tongtien, int page) {
    return donHangRepository.searchDonHang(username, diachi, tensanpham, tinhtrang, thanhtoan, tongtien, PageRequest.of(page, 10));
  }

  @Override
  public Optional<DonHang> findDonHangById(int madonhang) {
    return donHangRepository.findById(madonhang);
  }

  @Override
  public void saveDonHang(DonHang donHang) {
    donHangRepository.save(donHang);
  }

  @Override
  public void deleteDonHang(int madonhang) {
    donHangRepository.deleteById(madonhang);
  }

  @Override
  public Page<DonHang> getMyDonHang(String searchStr, String tinhtrang, String thanhtoan, String username, int page) {
    return donHangRepository.getMyDonHang(searchStr, tinhtrang, thanhtoan, username, PageRequest.of(page, 10));
  }
}
