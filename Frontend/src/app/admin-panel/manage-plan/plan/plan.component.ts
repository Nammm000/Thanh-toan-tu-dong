import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { PlanService } from 'src/app/service/plan.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-plan',
  templateUrl: './plan.component.html',
  styleUrls: ['./plan.component.scss'],
})
export class PlanComponent {
  data = {
    id: '',
    name: '',
    description: '',
    status: 'true',
    code: '',
    price: null,
    duration: null,
    createdTime: null,
  };
  page = 'add';

  constructor(
    private planService: PlanService,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.data.id = id;
      this.page = 'edit';
      this.getPlanDetails(parseInt(id));
    }
  }

  onChange(status: any) {
    this.data.status = status.toString();
  }

  newPlan() {
    this.ngxService.start();
    if (this.page == 'add') {
      this.planService.add(this.data).subscribe(
        (response: any) => {
          this.ngxService.stop();
          this.snackbarService.openSnackBar(
            'Plan added successfully',
            'success'
          );
          this.router.navigate(['/admin/plans']);
        },
        (error: any) => {
          this.ngxService.stop();
          console.log(error.error?.message);
        }
      );
    } else if (this.page == 'edit') {
      this.planService.update(this.data).subscribe(
        (response: any) => {
          this.ngxService.stop();
          this.snackbarService.openSnackBar(
            'Plan updated successfully',
            'success'
          );
          this.router.navigate(['/admin/plans']);
        },
        (error: any) => {
          this.ngxService.stop();
          console.log(error.error?.message);
        }
      );
    }
  }

  getPlanDetails(id: number) {
    this.ngxService.start();
    this.planService.getPlanById(id).subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.data.name = response.name;
        this.data.description = response.description;
        this.data.status = response.status;
        this.data.code = response.code;
        this.data.price = response.price;
        this.data.duration = response.duration;
        this.data.createdTime = response.createdTime;
        console.log(this.data);
      },
      (error: any) => {
        this.ngxService.stop();
        console.log(error.error?.message);
      }
    );
  }
}
