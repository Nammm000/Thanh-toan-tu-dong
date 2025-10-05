import { Component, OnDestroy, OnInit } from '@angular/core';
import { PlanService } from 'src/app/service/plan.service';
import { NewsService } from 'src/app/service/news.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, Editor, Toolbar } from "ngx-editor";
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import jsonDoc from 'src/app/model/doc';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent {

  data = {
    id: '',
    title: '',
    description: '',
    status: 'true',
    plan_code: '',
    content: '',
    createdTime: '',
    view: 0,
  };

  constructor(
    private planService: PlanService,
    private newsService: NewsService,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}
  
  ngOnInit(): void {
     const id = this.activatedRoute.snapshot.paramMap.get('id');
     if (id) {
       this.getNewsDetails(parseInt(id));
     }
  }

  getNewsDetails(id: number) {
    this.ngxService.start();
    this.newsService.getNewsById(id).subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.data.title = response.title;
        this.data.description = response.description;
        this.data.content = response.content;
        this.data.status = response.status;
        this.data.plan_code = response.plan_code;
        this.data.createdTime = response.createdTime;
        this.data.view = response.view;
        this.newsService.updateViews(id).subscribe((response: any) => {}, (error: any) => {
          console.log(error.error?.message);
        });
      },
      (error: any) => {
        this.ngxService.stop();
        console.log(error.error?.message);
      }
    );
  }

}
