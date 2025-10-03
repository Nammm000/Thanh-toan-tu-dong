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
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit, OnDestroy {

  responseMessage: any;
  listPlanCode: any[] = [];
  data = {
    id: '',
    title: '',
    description: '',
    status: 'true',
    plan_code: '',
    content: '',
    createdTime: '',
  };
  page = "add";

  editordoc = jsonDoc;
  editor: Editor = new Editor();
  toolbar: Toolbar = [
    ['bold', 'italic'],
    ['underline', 'strike'],
    ['code', 'blockquote'],
    ['ordered_list', 'bullet_list'],
    [{ heading: ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'] }],
    ['link', 'image'],
    ['text_color', 'background_color'],
    ['align_left', 'align_center', 'align_right', 'align_justify'],
  ];

  form = new FormGroup({
    editorContent: new FormControl(
      { value: jsonDoc, disabled: false },
      Validators.required()
    ),
  });

  // get doc(): AbstractControl {
  //   return this.form.get('editorContent')!;
  // }
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
     this.editor = new Editor();
    this.planService.getAllPlanCode().subscribe(
      (response: any) => {
        this.listPlanCode = response;
      }, (error: any) => {
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
    )
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.data.id = id;
      this.page = 'edit';
      this.getNewsDetails(parseInt(id));
    }
  }

  ngOnDestroy(): void {
    this.editor.destroy(); // Important: Destroy the editor instance
  }

  onChange(status: any) {
    this.data.status = status.toString();
  }

  addEditNews() {
    this.ngxService.start();
    // this.data.content = JSON.stringify(this.form.get('editorContent')?.value ?? '');
    if (this.page == 'add') {
      this.newsService.add(this.data).subscribe(
        (response: any) => {
          this.ngxService.stop();
          this.snackbarService.openSnackBar(
            'News added successfully',
            'success'
          );
          this.router.navigate(['/admin/news']);
        },
        (error: any) => {
          this.ngxService.stop();
          console.log(error.error?.message);
        }
      );
    } else if (this.page == 'edit') {
      this.newsService.update(this.data).subscribe(
        (response: any) => {
          this.ngxService.stop();
          this.snackbarService.openSnackBar(
            'News updated successfully',
            'success'
          );
          this.router.navigate(['/admin/news']);
        },
        (error: any) => {
          this.ngxService.stop();
          console.log(error.error?.message);
        }
      );
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
      },
      (error: any) => {
        this.ngxService.stop();
        console.log(error.error?.message);
      }
    );
  }
}
