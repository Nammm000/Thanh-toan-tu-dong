import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-head-news-card',
  templateUrl: './head-news-card.component.html',
  styleUrls: ['./head-news-card.component.scss']
})
export class HeadNewsCardComponent {

  @Input() news: any;

}
