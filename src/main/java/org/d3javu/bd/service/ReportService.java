package org.d3javu.bd.service;

import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.d3javu.bd.dto.post.PostReadDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

//@RequiredArgsConstructor
@Service
public class ReportService {

    private PostService postService;

    @Value("${app.report.bucket}")
    private String bucket;

    public ReportService(@Lazy PostService postService) {
        this.postService = postService;
    }

    @SneakyThrows
    public void exportReport(int n) {
        String targetPath = ResourceUtils.getURL("src/reports").getPath();
        var bestPosts = this.postService.findTopN(n);
        File reportTemplate = ResourceUtils.getFile("classpath:jasper/reporttemplates/source/Posts.jrxml");
        var report = JasperCompileManager.compileReportToFile(reportTemplate.getAbsolutePath());
//        File file = new File(ResourceUtils.getFile("jasper/reporttemplates/compiled").getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bestPosts);
        Map<String, Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
//        var name = Hashing.sha256().hashString(jasperPrint.getName(), Charset.defaultCharset()).toString();
        var name = Hashing.sha256().hashLong(bestPosts.stream().map(PostReadDto::hashCode).mapToLong(Long::valueOf).sum()).toString();
        JasperExportManager.exportReportToPdfFile(jasperPrint, new File(bucket).getAbsolutePath() +"\\"+name+".pdf");
//        return "";
    }

}