import { Component } from '@angular/core';
import { PlanService } from 'src/app/service/plan.service';
import { NewsService } from 'src/app/service/news.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { customFormattedDate, msToTime } from 'src/app/shared/utils';

@Component({
  selector: 'app-pricing',
  templateUrl: './pricing.component.html',
  styleUrls: ['./pricing.component.scss'],
})
export class PricingComponent {
  responseMessage: string = '';

  listPlanCode: any[] = [];

  userPay: any = 0;
  userExpirationDate: any = '';
  planName: string = '';

  userRole: any;
  token: any = localStorage.getItem('token');
  tokenPaload: any;

  customFormattedDate = customFormattedDate;

  constructor(
    private planService: PlanService,
    private newsService: NewsService,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.planService.getAllPlanCodeDescription().subscribe(
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
        if (response.price && response.price > 0) {
          this.userPay = response.price;
          this.userExpirationDate = response.expirationDate;
          this.getPlanName();
          // const plan = this.listPlanCode.filter((plan) => plan.price==this.userPay);
          // if (plan.length==1) {
          //   this.planName =  plan[0].name;
          // }
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

  timeLeft(expirationDate: any) {
    const exp = new Date(expirationDate);
    const now = new Date();
    if (now < exp) {
      let diff = exp.getTime() - now.getTime();
      const timeLeft = msToTime(diff);
      if (timeLeft.months && timeLeft.months > 0) {
        return timeLeft.months + ' tháng';
      } else if (timeLeft.days && timeLeft.days > 0) {
        return timeLeft.days + ' ngày';
      } else if (timeLeft.hours && timeLeft.hours > 0) {
        return timeLeft.hours + ' giờ';
      }
      return 0;
    }
    return null;
  }

  getPlanName() {
    if (this.userPay == 0) {
      return;
    }
    const plan = this.listPlanCode.filter((plan) => plan.price == this.userPay);

    //// let tempList: any[] = [];
    if (plan.length == 1) {
      this.planName = plan[0].name;
      const len = this.listPlanCode.length;
      const planIndex = this.listPlanCode.findIndex(
        (object) => object.code === plan[0].code
      );
      let mid = Math.floor(len / 2);
      if (planIndex != mid) {
        let temp = this.listPlanCode[planIndex];
        this.listPlanCode[planIndex] = this.listPlanCode[mid];
        this.listPlanCode[mid] = temp;
      }
    }
  }
}
