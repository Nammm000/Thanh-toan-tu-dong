import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProviderService } from 'src/app/service/provider.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
// import { CategoryService } from 'src/app/service/category.service';

@Component({
  selector: 'app-provider',
  // standalone: true,
  // imports: [CommonModule],
  templateUrl: './provider.component.html',
  styleUrls: ['./provider.component.scss']
})
export class ProviderComponent implements OnInit {

  onAddProvider = new EventEmitter();
  onEditProvider = new EventEmitter();
  providerForm: any = FormGroup;
  dialogAction: any = "Add";
  action: any = "Add";
  responseMessage: any;
  // categorys: any = [];

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
  private forBuilder: FormBuilder,
  private providerService: ProviderService,
  // private categoryService: CategoryService,
  public dialogRef: MatDialogRef<ProviderComponent>,
  private snackBarService: SnackbarService) { }

  ngOnInit(): void {
    this.providerForm = this.forBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      address: [null, [Validators.required]]
    });
    if (this.dialogData.action === 'Edit') {
      this.dialogAction = "Edit";
      this.action = "Update";
      this.providerForm.patchValue(this.dialogData.data);
    } 
    // this.gets();
  }

  // getCategorys() {
  //   this.categoryService.getCategorys().subscribe((response:any) => {
  //     this.categorys = response;
  //   }, (error: any) => {
  //     console.log(error);
  //     if (error.error?.message) {
  //       this.responseMessage = error.error?.message;
  //     } else {
  //       this.responseMessage = GlobalConstants.genericError;
  //     }
  //     this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
  //   })
  // }

  handleSubmit() {
    if (this.dialogAction === "Edit") {
      this.edit();
    } else {
      this.add();
    }
  }


  add() {
    var formData = this.providerForm.value;
    var data = {
      name: formData.name,
      address: formData.address
    }
    this.providerService.add(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onAddProvider.emit();
      this.responseMessage = response.message;
      this.snackBarService.openSnackBar(this.responseMessage, "Success");
    }, (error) => {
      this.dialogRef.close();
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }

  edit() {
    var formData = this.providerForm.value;
    var data = {
      id: this.dialogData.data.id,
      name: formData.name,
      address: formData.address
    }
    this.providerService.update(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onEditProvider.emit();
      this.responseMessage = response.message;
      this.snackBarService.openSnackBar(this.responseMessage, "Success");
    }, (error) => {
      this.dialogRef.close();
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }
}
