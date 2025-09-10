import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from 'src/app/service/customer.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
// import { CategoryService } from 'src/app/service/category.service';

@Component({
  selector: 'app-customer',
  // standalone: true,
  // imports: [CommonModule],
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {
  
  onAddCustomer = new EventEmitter();
  onEditCustomer = new EventEmitter();
  customerForm: any = FormGroup;
  dialogAction: any = "Add";
  action: any = "Add";
  responseMessage: any;
  // categorys: any = [];

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
  private forBuilder: FormBuilder,
  private customerService: CustomerService,
  // private categoryService: CategoryService,
  public dialogRef: MatDialogRef<CustomerComponent>,
  private snackBarService: SnackbarService) { }

  ngOnInit(): void {
    this.customerForm = this.forBuilder.group({
      username: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      contactNumber: [null, [Validators.required]],
      address: [null, [Validators.required]]
    });
    if (this.dialogData.action === 'Edit') {
      this.dialogAction = "Edit";
      this.action = "Update";
      this.customerForm.patchValue(this.dialogData.data);
    }
  }

  handleSubmit() {
    if (this.dialogAction === "Edit") {
      this.edit();
    } else {
      this.add();
    }
  }


  add() {
    var formData = this.customerForm.value;
    var data = {
      username: formData.username,
      contactNumber: formData.contactNumber,
      address: formData.address
    }
    this.customerService.add(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onAddCustomer.emit();
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
    var formData = this.customerForm.value;
    var data = {
      id: this.dialogData.data.id,
      username: formData.username,
      contactNumber: formData.contactNumber,
      address: formData.address
    }
    this.customerService.update(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onEditCustomer.emit();
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
