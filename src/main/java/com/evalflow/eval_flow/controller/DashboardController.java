// package com.evalflow.eval_flow.controller;

// import com.evalflow.eval_flow.service.DashboardService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.Map;

// @RestController
// @RequestMapping("/admin/api") // ✅ Changed from /admin/dashboard
// @RequiredArgsConstructor
// public class DashboardController {

// private final DashboardService dashboardService;

// @GetMapping("/dashboard/summary") // ✅ Full path:
// /admin/api/dashboard/summary
// public Map<String, Object> getDashboardSummary() {
// return dashboardService.getSummaryStats();
// }
// }