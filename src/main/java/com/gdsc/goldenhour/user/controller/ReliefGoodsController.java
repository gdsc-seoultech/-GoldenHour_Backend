package com.gdsc.goldenhour.user.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsCreateRes;
import com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsUpdateRes;
import com.gdsc.goldenhour.user.service.ReliefGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<ResponseDto<?>> createReliefGoods(@Valid @RequestBody ReliefGoodsReq reliefGoodsReq, @AuthenticationPrincipal String userId) {
        ReliefGoodsCreateRes reliefGoodsCreateRes = reliefGoodsService.createReliefGoods(reliefGoodsReq, userId);
        return new ResponseEntity<>(ResponseDto.success(reliefGoodsCreateRes), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> updateReliefGoods(@Valid @RequestBody ReliefGoodsReq reliefGoodsReq, @PathVariable Long id, @AuthenticationPrincipal String userId) {
        ReliefGoodsUpdateRes reliefGoodsUpdateRes = reliefGoodsService.updateReliefGoods(reliefGoodsReq, id, userId);
        return new ResponseEntity<>(ResponseDto.success(reliefGoodsUpdateRes), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> deleteReliefGoods(@PathVariable Long id, @AuthenticationPrincipal String userId) {
        reliefGoodsService.deleteReliefGoods(id, userId);
        return new ResponseEntity<>(ResponseDto.success("구호물품 삭제 완료"), HttpStatus.OK);
    }
}
