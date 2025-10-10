import { Component, OnDestroy, OnInit } from '@angular/core';
import { PlanService } from 'src/app/service/plan.service';
import { NewsService } from 'src/app/service/news.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { ActivatedRoute, Router } from '@angular/router';
// import { Editor } from "ngx-editor";
import { jwtDecode } from 'jwt-decode';
// import { showImg } from 'src/app/shared/utils';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent {

  responseMessage: string = "";
  
  listPublicNews: any[] = [];
  listCanReadNews: any[] = [];
  listCanNotReadNews: any[] = [];

  listHeadNews: any[] = [];
  listNews: any[] = [];
  tab = "public";
  planName: string = "";

  listPlanCode: any[] = [];

  userPay: any = 0;
  userRole: any;
  token: any = localStorage.getItem('token');
  tokenPaload: any;

  // editor: Editor = new Editor();
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
    this.getAllNews();
    this.planService.getAllPlanCode().subscribe(
      (response: any) => {
        this.listPlanCode = response;
      },
      (error: any) => {
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
    this.tokenPaload = jwtDecode(this.token);
    this.userRole = this.tokenPaload?.role + '';
    
    this.newsService.getUserSub().subscribe(
      (response: any) => {
        if (response.price && response.price>0) {
          this.userPay = response;
        }
        
      },
      (error: any) => {
        if (error.error?.text) {
          this.responseMessage = error.error?.text;
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

  getAllNews() {
    this.ngxService.start();
    this.newsService.getAllNews().subscribe(
      (response: any) => {
        this.ngxService.stop();
        const listNews = response;
        listNews.forEach((news: any) => {
          if (news.price==0) {
            this.listPublicNews.push(news);
          } else if (this.userPay >= news.price) {
            this.listCanReadNews.push(news);
          } else {
            this.listCanNotReadNews.push(news);
          }
        });
        this.listHeadNews = this.listPublicNews.splice(0, 3); // splice slice(giu nguyen mang goc)
        this.listNews = this.listPublicNews;
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
        this.listCanReadNews = response;
        this.listHeadNews = this.listCanReadNews.splice(0, 3);
      },
      (error: any) => {
        this.listCanReadNews = [];
        this.ngxService.stop();
        if (error.error?.text) {
          this.responseMessage = error.error?.text;
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
    // if (this.tab == 'public') {
    //   this.getPublicNews();
    // } else {
    //   this.getUserSubNews();
    // }
    if (this.tab == 'public') {
      this.getAllNews();
    } else if (this.tab == 'premium') {
      this.listHeadNews = this.listCanReadNews.splice(0, 3);
      this.listNews = this.listCanReadNews.concat(this.listCanNotReadNews);
      this.listCanNotReadNews = [];
      this.listCanReadNews = [];
      this.listPublicNews = [];
      this.listNews.sort((a, b) => (b.updatedTime.localeCompare(a.updatedTime)));
    }
  }

  check(price: any) {
    if (this.userRole=="ADMIN") {
      return false;
    }
    return price > this.userPay;
  }

  getPlanCode(price:any) {
    if (price==0) {
      return "";
    }
    const plan = this.listPlanCode.filter((plan) => plan.price==price);
    if (plan.length==1) {
      return plan[0].code;
    }
    return "";
  }

}
