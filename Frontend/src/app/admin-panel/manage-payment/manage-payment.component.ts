import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DomSanitizer } from '@angular/platform-browser';
import { ImageHandler } from 'src/app/model/image.model';
import { PlanService } from 'src/app/service/plan.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

import { showImg } from 'src/app/shared/utils';

@Component({
  selector: 'app-manage-payment',
  templateUrl: './manage-payment.component.html',
  styleUrls: ['./manage-payment.component.scss'],
})
export class ManagePaymentComponent {
  fileToUpload: any;
  imageUrl: any;
  responseMessage: any;
  listImages: any[] = [];
  showImg = showImg;

  constructor(
    private sanitizer: DomSanitizer,
    private planService: PlanService,
    private dialog: MatDialog,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.ngxService.start();
    this.setListPayment();
  }

  setListPayment() {
    this.ngxService.stop();
    this.planService.getPayments().subscribe(
      (response: any) => {
        this.listImages = response;
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
  }

  handleFileInput(target: any) {
    // console.log(target.files);

    const file: FileList = target.files;
    this.fileToUpload = file.item(0);

    // //Show image preview
    // let reader = new FileReader();
    // reader.onload = (event: any) => {
    //   this.imageUrl = event.target.result;
    // };
    // reader.readAsDataURL(this.fileToUpload);

    const imageHandler: ImageHandler = {
      file: this.fileToUpload,
      url: this.sanitizer.bypassSecurityTrustUrl(
        window.URL.createObjectURL(this.fileToUpload)
      ),
    };

    const imageHandlerList: ImageHandler[] = [];
    imageHandlerList.push(imageHandler);
    const formData = this.prepareFormData(imageHandlerList);

    this.ngxService.start();
    this.planService.addNew(formData).subscribe(
      (response: any) => {
        this.responseMessage = response?.message;
        this.snackbarService.openSnackBar(this.responseMessage, 'Success');
        this.setListPayment();
      },
      (error: any) => {
        this.ngxService.stop();
        console.log(error);
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = 'Something went wrong';
        }
        this.snackbarService.openSnackBar(this.responseMessage, 'Error');
      }
    );
  }

  // showImg(picBytes: any, imageType: any, imgName: any) {
  //   const byteString = window.atob(picBytes);
  //   const arrayBuffer = new ArrayBuffer(byteString.length);
  //   const int8Array = new Uint8Array(arrayBuffer);
  //   for (let i = 0; i < byteString.length; i++) {
  //     int8Array[i] = byteString.charCodeAt(i);
  //   }
  //   const blob = new Blob([int8Array], { type: imageType });
    
  //   const imgFile = new File([blob], imgName, { type: imageType });
    
  //   return this.sanitizer.bypassSecurityTrustUrl(
  //       window.URL.createObjectURL(imgFile)
  //     );
  // }

  prepareFormData(imageHandlerList: ImageHandler[]): FormData {
    const formData = new FormData();

    for (var i = 0; i < imageHandlerList.length; i++) {
      formData.append(
        'imageFile',
        imageHandlerList[i].file,
        imageHandlerList[i].file.name
      );
    }
    formData.append('attachedTo', 'payment');
    return formData;
  }

  deleteAction(id: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'delete ' + id + ' image',
      confirmation: true,
    };
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
      (response: any) => {
        this.ngxService.start();
        this.deletePlan(id);

        dialogRef.close();
      }
    );
  }

  deletePlan(id: any) {
    this.planService.deletePayments(id).subscribe(
      (response: any) => {
        this.responseMessage = response?.message;
        this.snackbarService.openSnackBar(this.responseMessage, 'Success');
        this.setListPayment();
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

  imgLink(imagePicByte: any, imageType: any, imageName: any) {
    return this.showImg(this.sanitizer, imagePicByte, imageType, imageName);
  }
}
