package com.example.kiprissearch.controller;

import com.example.kiprissearch.domain.utils.FileUtil;
import com.example.kiprissearch.service.WhitelistService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.kiprissearch.domain.utils.FileUtil.WHITELIST_FILE_NAME;

@Controller
@RequestMapping("/whitelist")
@RequiredArgsConstructor
public class WhitelistController {

    private final WhitelistService whitelistService;

    @GetMapping("")
    public String getWhitelist(Model model) {
        List<String> whitelist = whitelistService.getWhitelist();
        model.addAttribute("whitelist", whitelist);
        return "whitelist.html";
    }

    @PostMapping("/download")
    public void downloadAsFile(HttpServletResponse response) {
        FileUtil.downloadFile(response, WHITELIST_FILE_NAME, WHITELIST_FILE_NAME);
    }

    @PostMapping("/upload")
    public String uploadAsFile(@RequestParam("file") MultipartFile file) {
        whitelistService.update(file);
        return "redirect:/whitelist";
    }

    @PostMapping("/add")
    public String addToWhitelist(Model model,
                                 @RequestParam String keyword) {
        List<String> whitelist = whitelistService.add(keyword);

        model.addAttribute("whitelist", whitelist);
        return "redirect:/whitelist";
    }

    @PostMapping("/all")
    public String deleteAllWhitelist(Model model) {
        whitelistService.deleteAll();
        model.addAttribute("whitelist", null);
        return "redirect:/whitelist";
    }
}
