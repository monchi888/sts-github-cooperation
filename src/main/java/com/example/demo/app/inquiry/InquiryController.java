package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryNotFoundException;
import com.example.demo.service.InquiryService;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {

 	private final InquiryService inquiryService;

 	@Autowired
 	public InquiryController(InquiryService inquiryService){
 		this.inquiryService = inquiryService;
 	}

	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();

		/*
		 * InquiryNotFoundExceptionをスローさせるため用のコード
		 */
//		Inquiry inquiry = new Inquiry();
//		inquiry.setId(4);
//		inquiry.setName("Jamie");
//		inquiry.setEmail("jamie@example.com");
//		inquiry.setContents("Hello.");
//		inquiry.setCreated(LocalDateTime.now());
//		
//		inquiryService.update(inquiry);
		
//		try {
//			inquiryService.update(inquiry);
//		} catch (InquiryNotFoundException e) {
//			model.addAttribute("message", e);
//			return "error/CustomPage";
//		}
		
//		inquiryService.save(inquiry);
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "Inquiry Index");
		return "inquiry/index_boot";
	}

	/*
	 * formクラスを引数に設定することで、
	 * formの入力欄と紐づけられて自動的に初期化される
	 */
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm,
				Model model,
				@ModelAttribute("complete") String complete) {
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form_boot";
	}

	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "InquiryForm");
		return "inquiry/form_boot";
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
			return "inquiry/form_boot";
		}
		model.addAttribute("title", "Confirm Page");
		return "inquiry/confirm_boot";
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
		
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());
		inquiryService.save(inquiry);
		/*
		 * RedirectAttributesでデータを渡す際にはmodelが使えないため
		 * addFlashAttributeメソッドを使用
		 * addFlashAttributeメソッドは、
		 * リクエストを隔ててデータを保管する仕組みである「セッション」という機能を
		 * 内部的に使用している
		 * リダイレクトを使うのは、二重クリック問題に対応するため
		 * リダイレクトではリクエストをし直すので、
		 * リクエストスコープにデータを保管しておいても次のリクエスト時には失われる
		 */
		redirectAttributes.addFlashAttribute("complete", "Registerd");
		/*
		 * この「redirect:」～は、
		 * HTMLを指しているの「ではなく」
		 * URLを指している
		 */
		return "redirect:/inquiry/form";
	}
	
	/*
	 * indexメソッドでIDが存在しないデータを登録しようとした場合の例外を
	 * 「同じcontroller内のメソッドとして外出し」したversion
	 * 
	 */
//	@ExceptionHandler(InquiryNotFoundException.class)
//	public String handleException(InquiryNotFoundException e, Model model) {
//		model.addAttribute("message", e);
//		return "error/CustomPage";
//	}
}
