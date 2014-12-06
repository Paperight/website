package com.paperight.mvc.util;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public class Pagination {

    public static void buildPaginationModel(Model model, Page<?> page) {
        int currentIndex = page.getNumber();
        int beginIndex = Math.max(0, currentIndex - 5);
        int endIndex = Math.min(currentIndex + 10, page.getTotalPages() - 1);
        if (endIndex < 0) {
            endIndex = 0;
        }
        model.addAttribute("beginIndex", beginIndex);
        model.addAttribute("endIndex", endIndex);
        model.addAttribute("currentIndex", currentIndex);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("pageItemCount", page.getNumberOfElements());
        model.addAttribute("totalItemCount", page.getTotalElements());
    }
    
}
