package com.example.demo.app.inquiry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {

// 	private final InquiryService inquiryService;

	//Add an annotation here
// 	public InquiryController(InquiryService inquiryService){
// 		this.inquiryService = inquiryService;
// 	}

	/*
	 * formクラスを引数に設定することで、
	 * formの入力欄と紐づけられて
	 * 自動的に初期化される
	 */
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm,
				Model model,
				@ModelAttribute("complete") String complete) {
		
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form";
	}

	@GetMapping
	public String index(Model model) {

		//hands-on

		return "inquiry/index";
	}

	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "InquiryForm");
		return "inquiry/form";
	}

	/*
	 * BindingResultにはvalidationをかけた後の結果が入ってくる
	 */
	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm,
				BindingResult result,
				Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "InquiryForm");
			return "inquiry/form";
		}
		model.addAttribute("title", "Confirm Page");
		return "inquiry/confirm";
	}

	/*
	 * RedirectAttributesはフラッシュスコープ（※）を実現するために必要
	 * ※リダイレクト後に一度だけ値を保持して表示できる
	 */
	@PostMapping("/complete")
	public String complete(@Validated InquiryForm inquiryForm,
				BindingResult result,
				Model model,
				RedirectAttributes redirectAttributes) {
		
		if(result.hasErrors()) {
			model.addAttribute("title", "InquiryForm");
			return "inquiry/form";
		}
		/*
		 * RedirectAttributesでデータを渡す際にはmodelが使えないため
		 * addFlashAttributeメソッドを使用
		 * リダイレクトではリクエストをし直すので、
		 * リクエストスコープにデータを保管しておいても
		 * 次のリクエスト時には失われる
		 * addFlashAttributeメソッドは、リクエストを隔てて
		 * データを保管する仕組みである「セッション」という機能を
		 * 内部的に使用している
		 * リダイレクトを使うのは、二重クリック問題に対応するため
		 */
		redirectAttributes.addFlashAttribute("complete", "Registerd");
		/*
		 * この「redirect:」～は、
		 * HTMLを指しているの「ではなく」
		 * URLを指している
		 */
		return "redirect:/inquiry/form";
	}

}
