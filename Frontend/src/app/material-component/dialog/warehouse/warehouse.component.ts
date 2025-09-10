import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { WarehouseService } from 'src/app/service/warehouse.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { LocationService } from 'src/app/service/location.service';

@Component({
  selector: 'app-warehouse',
  // standalone: true,
  // imports: [CommonModule],
  templateUrl: './warehouse.component.html',
  styleUrls: ['./warehouse.component.scss']
})
export class WarehouseComponent implements OnInit {

  onAddWarehouse = new EventEmitter();
  onEditWarehouse = new EventEmitter();
  warehouseForm: any = FormGroup;
  dialogAction: any = "Add";
  action: any = "Add";
  responseMessage: any;
  locations: any = [];

  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
  private forBuilder: FormBuilder,
  private warehouseService: WarehouseService,
  private locationService: LocationService,
  public dialogRef: MatDialogRef<WarehouseComponent>,
  private snackBarService: SnackbarService) { }

  ngOnInit(): void {
    this.warehouseForm = this.forBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      locationId: [null, [Validators.required]],
    });
    if (this.dialogData.action === 'Edit') {
      this.dialogAction = "Edit";
      this.action = "Update";
      this.warehouseForm.patchValue(this.dialogData.data);
    } 
    this.getLocations();
  }

  getLocations() {
    this.locationService.getLocations().subscribe((response:any) => {
      this.locations = response;
    }, (error: any) => {
      console.log(error);
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    })
  }

  handleSubmit() {
    if (this.dialogAction === "Edit") {
      this.edit();
    } else {
      this.add();
    }
  }


  add() {
    var formData = this.warehouseForm.value;
    var data = {
      name: formData.name,
      locationId: formData.locationId
    }
    this.warehouseService.add(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onAddWarehouse.emit();
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
    var formData = this.warehouseForm.value;
    var data = {
      id: this.dialogData.data.id,
      name: formData.name,
      locationId: formData.locationId
    }
    this.warehouseService.update(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onEditWarehouse.emit();
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
