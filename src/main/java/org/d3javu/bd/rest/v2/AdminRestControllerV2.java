package org.d3javu.bd.rest.v2;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.d3javu.bd.kafka.KafkaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

@RequiredArgsConstructor
@RequestMapping("/api/v2/admin")
@RestController
public class AdminRestControllerV2 {

    private final KafkaService kafkaService;

    @GetMapping(value = "/report")
    ResponseEntity<byte[]> getTopN(@RequestParam Integer n) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        var filename = Hashing.sha256().hashLong(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()) + ".pdf";

        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        return new ResponseEntity<>(this.kafkaService.getPdfOfTopN(n), headers, HttpStatus.OK);
    }

}
