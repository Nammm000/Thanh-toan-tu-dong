import { Component, Input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { showImg, customFormattedDate } from 'src/app/shared/utils';

@Component({
  selector: 'app-news-card',
  templateUrl: './news-card.component.html',
  styleUrls: ['./news-card.component.scss']
})
export class NewsCardComponent {

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
