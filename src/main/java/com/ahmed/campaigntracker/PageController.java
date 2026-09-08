package com.ahmed.campaigntracker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PageController {

    private final CampaignRepository campaignRepository;

    public PageController(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Campaign> campaigns = campaignRepository.findAll();
        model.addAttribute("campaigns", campaigns);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadExcel(
            @RequestParam("file") MultipartFile file,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ExcelReader reader = new ExcelReader();
            List<Campaign> campaigns = reader.readFile(file.getInputStream());

            model.addAttribute("campaigns", campaigns);

            return "preview";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Could not import file. Make sure this is a valid TikTok campaign export."
            );
            return "redirect:/";
        }
    }

    @PostMapping("/campaign/save")
    public String saveCampaign(
            @RequestParam String title,
            @RequestParam String sparkCode,
            @RequestParam int clicks,
            @RequestParam double spend,
            @RequestParam double ctr,
            @RequestParam int conversions,
            @RequestParam int impressions,
            @RequestParam double cpm,
            @RequestParam double cpc,
            RedirectAttributes redirectAttributes
    ) {

        Campaign campaign = new Campaign(
                title,
                sparkCode,
                clicks,
                spend,
                ctr,
                conversions,
                impressions,
                cpm,
                cpc
        );
        if (campaignRepository.existsByTitle(title)) {
            redirectAttributes.addFlashAttribute("error", "Campaign already exists");

            return "redirect:/";
        }
        campaignRepository.save(campaign);

        return "redirect:/";
    }

    @GetMapping("/campaign/{id}")
    public String viewCampaign(
            @PathVariable Long id,
            Model model
    ) {
        Campaign campaign = campaignRepository.findById(id).orElse(null);
        if (campaign == null) {
            return "redirect:/";
        }

        model.addAttribute("campaign", campaign);

        return "campaign-detail";
    }

    @PostMapping("/campaign/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam CampaignStatus status
    ) {
        Campaign campaign = campaignRepository.findById(id).orElse(null);
        if (campaign == null) {
            return "redirect:/";
        }
        campaign.setStatus(status);

        campaignRepository.save(campaign);
        return "redirect:/campaign/" + id;
    }

    @PostMapping("/campaign/{id}/delete")
    public String deleteCampaign(
            @PathVariable Long id
    ) {
        if (!campaignRepository.existsById(id)) {
            return "redirect:/";
        }
        campaignRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/campaign/{id}/spark-code")
    public String updateSparkCode(
            @PathVariable Long id,
            @RequestParam String sparkCode
    ) {
        Campaign campaign = campaignRepository.findById(id).orElse(null);

        if (campaign == null) {
            return "redirect:/";
        }
        campaign.setSparkCode(sparkCode);
        campaignRepository.save(campaign);
        return "redirect:/campaign/" + id;
    }

    @PostMapping("/campaign/{id}/assumptions")
    public String updateAssumptions(
            @PathVariable Long id,
            @RequestParam double landedRate,
            @RequestParam double epc
    ) {
        Campaign campaign = campaignRepository.findById(id).orElse(null);

        if (campaign == null) {
            return "redirect:/";
        }

        campaign.setLandedRate(landedRate);
        campaign.setEpc(epc);

        campaignRepository.save(campaign);

        return "redirect:/campaign/" + id;
    }
}