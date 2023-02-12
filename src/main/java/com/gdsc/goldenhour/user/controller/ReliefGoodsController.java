package com.gdsc.goldenhour.user.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import com.gdsc.goldenhour.user.service.ReliefGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/relief_goods")
public class ReliefGoodsController {

    private final ReliefGoodsService reliefGoodsService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> readReliefGoodsList(@AuthenticationPrincipal String userId) {
        return new ResponseEntity<>(ResponseDto.success(reliefGoodsService.readReliefGoodsList(userId)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createReliefGoods(@RequestBody ReliefGoodsReq reliefGoodsReq, @AuthenticationPrincipal String userId) {
        reliefGoodsService.createReliefGoods(reliefGoodsReq, userId);
        return new ResponseEntity<>(ResponseDto.success("구호물품 등록 완료"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> updateReliefGoods(@RequestBody ReliefGoodsReq reliefGoodsReq, @PathVariable Long id, @AuthenticationPrincipal String userId) {
        reliefGoodsService.updateReliefGoods(reliefGoodsReq, id, userId);
        return new ResponseEntity<>(ResponseDto.success("구호물품 수정 완료"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> deleteReliefGoods(@PathVariable Long id, @AuthenticationPrincipal String userId) {
        reliefGoodsService.deleteReliefGoods(id, userId);
        return new ResponseEntity<>(ResponseDto.success("구호물품 삭제 완료"), HttpStatus.OK);
    }
}
