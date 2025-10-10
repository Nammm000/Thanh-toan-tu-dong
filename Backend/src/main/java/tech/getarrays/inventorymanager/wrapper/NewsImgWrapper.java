package tech.getarrays.inventorymanager.wrapper;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewsImgWrapper {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String plan_code;
    private String status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Integer view;
    private Float price;

    private Long idImg;
    private String nameImg;
    private String typeImg;
    private byte[] picByteImg;

    public NewsImgWrapper(Long id, String title, String description, String content,
                       String plan_code, String status, LocalDateTime createdTime,
                       LocalDateTime updatedTime, Integer view, Float price, Long idImg,
                       String nameImg, String typeImg, byte[] picByteImg) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.plan_code = plan_code;
        this.status = status;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.view = view;
        this.price = price;

        this.idImg = idImg;
        this.nameImg = nameImg;
        this.typeImg = typeImg;
        this.picByteImg = picByteImg;
    }

    public NewsImgWrapper(Long id, String title, String description,
                          String status, LocalDateTime updatedTime,
                          Integer view, Float price,
                          String nameImg, String typeImg, byte[] picByteImg) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.updatedTime = updatedTime;
        this.view = view;
        this.price = price;

        this.nameImg = nameImg;
        this.typeImg = typeImg;
        this.picByteImg = picByteImg;
    }

}