import { Component, OnDestroy, OnInit } from '@angular/core';
import { PlanService } from 'src/app/service/plan.service';
import { NewsService } from 'src/app/service/news.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, Editor, Toolbar } from 'ngx-editor';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import jsonDoc from 'src/app/model/doc';
import { ImageHandler } from 'src/app/model/image.model';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss'],
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
    content: 'Nhập nội dung ...',
    createdTime: '',
    updatedTime: '',
    view: 0,
    idImg: '',
  };
  page = 'add';

  fileToUpload: any;
  imageUrl: any;

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

  // form = new FormGroup({
  //   editorContent: new FormControl(
  //     { value: jsonDoc, disabled: false },
  //     Validators.required()
  //   ),
  // });

  // get doc(): AbstractControl {
  //   return this.form.get('editorContent')!;
  // }
  // html: string = "";

  constructor(
    private planService: PlanService,
    private newsService: NewsService,
    private sanitizer: DomSanitizer,
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
    const formData = this.prepareFormData();
    if (this.page == 'add') {
      this.newsService.add(formData).subscribe(
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
      this.newsService.update(formData).subscribe(
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
        this.data.updatedTime = response.updatedTime;
        this.data.view = response.view;
        this.data.idImg = response.idImg;

        const byteString = window.atob(response.picByteImg);
        const arrayBuffer = new ArrayBuffer(byteString.length);
        const int8Array = new Uint8Array(arrayBuffer);
        for (let i = 0; i < byteString.length; i++) {
          int8Array[i] = byteString.charCodeAt(i);
        }
        const blob = new Blob([int8Array], { type: response.typeImg });
        const imgFile = new File([blob], response.nameImg, {
          type: response.typeImg,
        });
        this.fileToUpload = imgFile;
        this.imageUrl = this.sanitizer.bypassSecurityTrustUrl(
          window.URL.createObjectURL(imgFile)
        );
      },
      (error: any) => {
        this.ngxService.stop();
        console.log(error.error?.message);
      }
    );
  }

  handleFileInput(target: any) {
    const file: FileList = target.files;
    this.fileToUpload = file.item(0);

    //Show image preview
    let reader = new FileReader();
    reader.onload = (event: any) => {
      this.imageUrl = event.target.result;
    };
    reader.readAsDataURL(this.fileToUpload);
  }

  prepareFormData(): FormData {
    const imageHandler: ImageHandler = {
      file: this.fileToUpload,
      url: this.sanitizer.bypassSecurityTrustUrl(
        window.URL.createObjectURL(this.fileToUpload)
      ),
    };

    const imageHandlerList: ImageHandler[] = [];
    imageHandlerList.push(imageHandler);

    const formData = new FormData();

    for (var i = 0; i < imageHandlerList.length; i++) {
      formData.append(
        'imageFile',
        imageHandlerList[i].file,
        imageHandlerList[i].file.name
      );
    }
    formData.append('attachedTo', 'news');
    formData.append(
      'data',
      new Blob([JSON.stringify(this.data)], { type: 'application/json' })
    );
    return formData;
  }
}
