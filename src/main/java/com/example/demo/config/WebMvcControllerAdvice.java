package com.example.demo.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
// メソッド実行前のタイミングで共通処理を実行したいときに
// @ControllerAdvice
// アノテーションを付与しておくだけで中の処理が実行される
public class WebMvcControllerAdvice {

	/*
	 * This method changes empty character to null
	 * このメソッドはリクエストとして空文字が送信された場合にそれをnullへ変換する
	 */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public String handleException(EmptyResultDataAccessException e,Model model) {
		model.addAttribute("message", e);
		return "error/CustomPage";
	}
	
//	@ExceptionHandler(InquiryNotFoundException.class)
//	public String handleException(InquiryNotFoundException e,Model model) {
//		model.addAttribute("message", e);
//		return "error/CustomPage";
//	}
   
}
