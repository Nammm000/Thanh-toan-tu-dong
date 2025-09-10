import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { LocationService } from 'src/app/service/location.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-location',
  // standalone: true,
  // imports: [CommonModule],
  templateUrl: './location.component.html',
  styleUrls: ['./location.component.scss']
})
export class LocationComponent implements OnInit {

  onAddLocation = new EventEmitter();
  onEditLocation = new EventEmitter();
  locationForm: any = FormGroup;
  dialogAction: any = "Add";
  acttion: any = "Add";
  responseMessage: any;


  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any,
    private forBuilder: FormBuilder,
    private categoyService: LocationService,
    public dialogRef: MatDialogRef<LocationComponent>,
    private snackBarService: SnackbarService
  ) { }

  ngOnInit(): void {
    this.locationForm = this.forBuilder.group({
      name: [null, [Validators.required]],
      address: [null, [Validators.required]]
    });
    if (this.dialogData.action === 'Edit') {
      this.dialogAction = "Edit";
      this.acttion = "Update";
      this.locationForm.patchValue(this.dialogData.data);
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
    var formData = this.locationForm.value;
    var data = {
      name: formData.name,
      address: formData.address,
    }
    this.categoyService.add(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onAddLocation.emit();
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

    var formData = this.locationForm.value;
    var data = {
      id: this.dialogData.data.id,
      name: formData.name,
      address: formData.address
    }
    this.categoyService.update(data).subscribe((response: any) => {
      this.dialogRef.close();
      this.onAddLocation.emit();
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
