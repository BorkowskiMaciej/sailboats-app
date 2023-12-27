package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boats")
public class BoatsController {

    private final BoatService boatService;
    private final UserService userService;

    @GetMapping()
    public String listBoats(Model model) {
        List<Boat> boats = boatService.findAllByOwnerId(userService.getAuthenticatedUserId());
        model.addAttribute("boats", boats);
        return "boats/list";
    }

    @GetMapping("/add")
    public String showAddBoatForm(Model model) {
        model.addAttribute("boat", new Boat());
        return "boats/add";
    }

    @PostMapping("/add")
    public String addBoat(@Valid Boat boat, BindingResult result, @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if(boatService.checkIfBoatExists(boat.getName())) {
            result.rejectValue("name", "name", "Łódź o takiej nazwie już istnieje");
            return "boats/add";
        }
        if (result.hasErrors()) {
            return "boats/add";
        }
        if (!file.isEmpty()) {
            try {
                boat.setImage(file.getBytes());
                boat.setImageName(file.getOriginalFilename());
            } catch (IOException e) {
                result.rejectValue("image", "image", "Wystąpił błąd z przetworzeniem zdjęcia");
                return "boats/add";
            }
        }
        boat.setOwnerId(userService.getAuthenticatedUserId());
        boatService.addBoat(boat);
        redirectAttributes.addFlashAttribute("successMessage", "Łódź została pomyślnie dodana.");
        return "redirect:/boats";
    }

    @GetMapping("/image/{boatId}")
    public ResponseEntity<byte[]> getBoatImage(@PathVariable Long boatId) {
        Boat boat = boatService.findById(boatId);
        if (boat != null && boat.getImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(boat.getImage(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Boat boat = boatService.findById(id);
        model.addAttribute("boat", boat);
        return "boats/edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteBoat(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boatService.deleteBoat(id);
        redirectAttributes.addFlashAttribute("successMessage", "Łódź została usunięta.");
        return "redirect:/boats";
    }

    @PostMapping("/update/{id}")
    public String updateBoat(@Valid Boat boat, BindingResult result,
            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "boats/edit";
        }
        try {
            if (!file.isEmpty()) {
                boat.setImage(file.getBytes());
                boat.setImageName(file.getOriginalFilename());
            }
            boat.setOwnerId(userService.getAuthenticatedUserId());
            boatService.updateBoat(boat);
            redirectAttributes.addFlashAttribute("successMessage", "Łódź została zaktualizowana.");
        } catch (IOException e) {
            result.rejectValue("image", "image", "Wystąpił błąd podczas przetwarzania zdjęcia");
            return "boats/edit";
        }

        return "redirect:/boats";
    }

}
