import { Component, Input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { showImg, customFormattedDate } from 'src/app/shared/utils';

@Component({
  selector: 'app-head-news-card',
  templateUrl: './head-news-card.component.html',
  styleUrls: ['./head-news-card.component.scss']
})
export class HeadNewsCardComponent {

  @Input() news: any;
  @Input() canNotRead: boolean = false;
  @Input() planCode: string = "";

  showImg = showImg;
  customFormattedDate = customFormattedDate;

    constructor(
        private sanitizer: DomSanitizer,
      ) {}
  
    imgLink(imagePicByte: any, imageType: any, imageName: any) {
    return this.showImg(this.sanitizer, imagePicByte, imageType, imageName);
  }

}
