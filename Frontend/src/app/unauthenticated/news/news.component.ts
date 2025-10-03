import { Component, OnDestroy, OnInit } from '@angular/core';
import { PlanService } from 'src/app/service/plan.service';
import { NewsService } from 'src/app/service/news.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Editor } from "ngx-editor";
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent {

  responseMessage: string = "";
  listNews: any[] = [];
  listHeadNews: any[] = [];
  tab = "public";
  planName: string = "";

  userRole: any;
  token: any = localStorage.getItem('token');
  tokenPaload: any;

  editor: Editor = new Editor();
  // html: string = "";

  constructor(
    private planService: PlanService,
    private newsService: NewsService,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}
  
  ngOnInit(): void {
    this.getPublicNews();
    this.tokenPaload = jwtDecode(this.token);
    this.userRole = this.tokenPaload?.role + '';
  }

  getPublicNews() {
    this.ngxService.start();
    this.newsService.getPublicNews().subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.listNews = response;
        this.listHeadNews = this.listNews.splice(0, 3);
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(
          this.responseMessage,
          GlobalConstants.error
        );
      }
    );
  }

  getUserSubNews() {
    this.ngxService.start();
    this.newsService.getUserSubNews().subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.listNews = response;
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(
          this.responseMessage,
          GlobalConstants.error
        );
      }
    );
  }
  
  changeTab(tab: string) {
    this.tab = tab;
    if (this.tab == 'public') {
      this.getPublicNews();
    } else {
      this.getUserSubNews();
    }
  }

}
