package br.social.impacthub.infrastructure.web;

import br.social.impacthub.model.dto.EmailValidatorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "emailValidator", url = "https://emailvalidatr.com")
public interface EmailValidatorClient {
    @GetMapping("/api/validate")
    EmailValidatorResponse validate(@RequestParam String email);
}
