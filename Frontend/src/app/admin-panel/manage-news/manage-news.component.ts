import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NewsService } from 'src/app/service/news.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-manage-news',
  templateUrl: './manage-news.component.html',
  styleUrls: ['./manage-news.component.scss']
})
export class ManageNewsComponent {

  dataSource: any;
  responseMessage: any;
  listNews: any[] = [];

  constructor(private newsService: NewsService,
      private ngxService: NgxUiLoaderService,
      private snackbarService: SnackbarService,
      private dialog: MatDialog,
      private router: Router) { }
  
    ngOnInit(): void {
      this.setListNews();
    }
  
    setListNews() {
      this.newsService.getNews().subscribe((response:any) => {
        // this.dataSource = new MatTableDataSource(response);
        this.listNews = response;
      }, (error:any) => {
        console.log(error.error?.message);
        if (error.error?.message) {
          this.responseMessage = error.error?.message; 
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      })
    }

    deleteAction(id: any) {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.data = {
          message: 'delete ' + id + ' news',
          condirmation: true
        };
        const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
        const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
          (response: any) => {
            this.ngxService.start();
            this.deleteNews(id);
            
            dialogRef.close();
          }
        );
      }

    deleteNews(id:any) {
      this.newsService.delete(id).subscribe((response:any) => {
        this.responseMessage = response?.message;
        this.snackbarService.openSnackBar(this.responseMessage, "success");
        this.setListNews();
      }, (error:any) => {
        console.log(error.error?.message);
        if (error.error?.message) {
          this.responseMessage = error.error?.message; 
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      });
    }

}
