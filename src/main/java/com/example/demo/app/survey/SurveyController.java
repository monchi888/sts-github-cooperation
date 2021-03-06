package com.example.demo.app.survey;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.entity.Survey;
import com.example.demo.service.SurveyService;

@Controller
@RequestMapping("/survey")
public class SurveyController {
	
	private final SurveyService surveyService;
	
	@Autowired
	public SurveyController(SurveyService surveyService){
		this.surveyService = surveyService;
	}
	
	@GetMapping
	public String index(Model model) {
		/*
		 * 次の3行がないとindex.html側にsurveyListが表示されないので要注意
		 */
		List<Survey> list = surveyService.getAll();
		model.addAttribute("surveyList", list);
		model.addAttribute("title", "Survey Index");
		return "survey/index";
	}
	
	/*
	 * formクラスを引数に設定することで、
	 * formの入力欄と紐づけられて自動的に初期化される
	 */
	@GetMapping("/form")
	public String form(SurveyForm surveyForm,
				Model model,
				@ModelAttribute("complete") String complete) {
		model.addAttribute("title", "Survey Form");
		return "survey/form";
	}
	
	@PostMapping("/form")
	public String form(SurveyForm surveyForm, Model model) {
		model.addAttribute("title", "Survey Form");
		return "survey/form";
	}
	
	/*
	 * BindingResultにはvalidationをかけた後の結果が入ってくる
	 */
	@PostMapping("/confirm")
	public String confirm(@Validated SurveyForm surveyForm,
				BindingResult result,
				Model model) {
		if(result.hasErrors()) {
			model.addAttribute("title", "SurveyForm");
			return "survey/form";
		}
		model.addAttribute("title", "Confirm Page");
		return "survey/confirm";
	}
	
	/*
	 * RedirectAttributesはフラッシュスコープ（※）を実現するために必要
	 * ※リダイレクト後に一度だけ値を保持して表示できる
	 */
	@PostMapping("/complete")
	public String complete(@Validated SurveyForm surveyForm,
				BindingResult result,
				Model model,
				RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			model.addAttribute("title", "SurveyForm");
			return "survey/form";
		}
		
		Survey survey = new Survey();
		survey.setAge(surveyForm.getAge());
		survey.setSatisfaction(surveyForm.getSatisfaction());
		survey.setComment(surveyForm.getComment());
		survey.setCreated(LocalDateTime.now());
		surveyService.save(survey);
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
		redirectAttributes.addFlashAttribute("complete",
				"Thank you for your cooperation!");
		/*
		 * この「redirect:」～は、
		 * HTMLを指しているの「ではなく」
		 * URLを指している
		 */
		return "redirect:/survey/form";
	}
}
