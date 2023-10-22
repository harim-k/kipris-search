package com.example.kiprissearch.controller;

import com.example.kiprissearch.controller.to.SearchResponse;
import com.example.kiprissearch.domain.utils.ExcelUtil;
import com.example.kiprissearch.domain.utils.FileUtil;
import com.example.kiprissearch.domain.utils.SearchResult;
import com.example.kiprissearch.service.KiprisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;


@Controller
@AllArgsConstructor
@RequestMapping("/kipris")
public class KiprisController {

    private KiprisService kiprisService;

    @GetMapping("")
    public String index(Model model) {
        List<String> completedFilenames = kiprisService.getCompletedFilenames();
        List<String> inProgressFilenames = kiprisService.getInProgressFilenames();
        String apiKey = kiprisService.getApiKey();

        model.addAttribute("completedFiles", completedFilenames);
        model.addAttribute("inProgressFiles", inProgressFilenames);
        model.addAttribute("apiKey", apiKey);

        return "kipris.html";
    }

    @PostMapping("/search")
    public void search(HttpServletResponse response,
                       @RequestParam("file") MultipartFile file) {
        Map<String, Integer> keywordMap = ExcelUtil.extractKeywordMap(file);

        String filename = kiprisService.getDownloadFilename(file.getOriginalFilename());

        kiprisService.writeInProgressFile(filename);
        SearchResponse searchResponse = kiprisService.searchLikeWithoutWhitelist(keywordMap);
        kiprisService.removeInProgressFile(filename);

        ExcelUtil.markKeywordsAndSaveAsFile(file, searchResponse);
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        FileUtil.downloadFile(response, filename);
    }

    @PostMapping("/search/{keyword}")
    public SearchResult search(@PathVariable("keyword") String keyword) {
        return kiprisService.searchLike(keyword);
    }



    @GetMapping("/file/download/{filename}")
    public void downloadFile(HttpServletResponse response,
                             @PathVariable("filename") String filename) {
        FileUtil.downloadFile(response, filename);
    }

    @PostMapping("/file/all")
    public String deleteAllWhitelist(Model model) {
        kiprisService.deleteAll();
        model.addAttribute("files", null);

        return "redirect:/kipris";
    }

    @GetMapping("/apiKey")
    public String getApiKey() {
        kiprisService.getApiKey();
        return "redirect:/kipris";
    }

    @PostMapping("/apiKey")
    public String changeApiKey(@RequestParam String apiKey) {
        kiprisService.changeApiKey(apiKey);
        return "redirect:/kipris";
    }
}
