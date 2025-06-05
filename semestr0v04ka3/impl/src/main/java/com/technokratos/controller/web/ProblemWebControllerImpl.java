package com.technokratos.controller.web;

import com.technokratos.dto.response.problem.ProblemFileContentResponse;
import com.technokratos.dto.response.ProblemCreateDto;
import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.service.sub.DifficultyLevelService;
import com.technokratos.service.general.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/problems")
@Slf4j
public class ProblemWebControllerImpl {
    private static final String CREATE_VIEW = "problems/create";
    private static final String CREATE_SUCCESS_VIEW = "problems/create-success";
    private static final String MAIN_VIEW = "problems/main";

    private final ProblemService problemService;
    private final DifficultyLevelService difficultyLevelService;

    @GetMapping("/create")
    public String showCreateForm(@RequestParam(name = "success", required = false) String success,
                                 Model model) {

        if (success != null) {
            return CREATE_SUCCESS_VIEW;
        }

        model.addAttribute("problemCreate", problemCreateDto(problemService.getTemplates()));
        model.addAttribute("difficultyLevel", difficultyLevelService.findAll());

        return CREATE_VIEW;
    }

    @PostMapping("/create")
    public String createProblem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute("problemCreate") @Valid ProblemCreateDto problemCreateDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("difficultyLevel", difficultyLevelService.findAll());
            return CREATE_VIEW;
        }

        try {
            problemService.create(userDetails, problemCreateDto);
            return "redirect:/problems/create?success";
        } catch (Exception e) {
            log.error("Problem creation failed", e);

            model.addAttribute("error", "Something went wrong. Try again later.");
            model.addAttribute("difficultyLevel", difficultyLevelService.findAll());
            return CREATE_VIEW;
        }

    }

    @GetMapping()
    public String getALl(Model model,
                         @PageableDefault(size = 12, sort = "title") Pageable pageable) {

        model.addAttribute("problems", problemService.getAll(pageable));
        return MAIN_VIEW;
    }


    private ProblemCreateDto problemCreateDto(ProblemFileContentResponse templates) {
        return ProblemCreateDto.builder()
                .description(templates.getDescription())
                .solutionTest(templates.getSolutionTest())
                .solutionTemplate(templates.getSolutionTemplate())
                .build();
    }
}
