package ir.tejarattrd.arshiaghandi.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @GetMapping("/portfolio")
    public ResponseEntity<?> getPortfolioData(Principal principal) {
        // 'principal' به صورت خودکار توسط Spring Security با اطلاعات کاربر احراز هویت شده پر می‌شود.
        // principal.getName() نام کاربری را برمی‌گرداند.
        if (principal == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        // در یک سناریوی واقعی، شما از دیتابیس اطلاعات را واکشی می‌کنید
        // در اینجا ما داده‌های نمونه را برمی‌گردانیم
        Map<String, String> portfolioData = Map.of(
                "username", principal.getName(),
                "portfolioValue", "۱,۴۵۰,۸۳۰,۰۰۰"
        );

        return ResponseEntity.ok(portfolioData);
    }
}