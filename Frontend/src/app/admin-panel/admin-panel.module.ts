import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CdkTableModule } from '@angular/cdk/table';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';

import { AdminPanelRoutes } from './admin-panel.routing';
// import { MaterialModule } from '../shared/material-module';
import { ManageNewsComponent } from './manage-news/manage-news.component';
import { ManagePlanComponent } from './manage-plan/manage-plan.component';
import { NewsComponent } from './manage-news/news/news.component';
import { PlanComponent } from './manage-plan/plan/plan.component';
import { MaterialModule } from '../shared/material-module';
import { UserComponent } from './manage-user/user/user.component';
import { ManageSubscriptionComponent } from './manage-subscription/manage-subscription.component';
// import { ViewBillProductsComponent } from './dialog/view-bill-products/view-bill-products.component';
import { NgxEditorModule } from "ngx-editor";
import { ManagePaymentComponent } from './manage-payment/manage-payment.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminPanelRoutes),
    MaterialModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgxEditorModule,
    FlexLayoutModule,
    CdkTableModule
  ],
  providers: [],
  declarations: [
    // ViewBillProductsComponent
  
    ManageNewsComponent,
    ManagePlanComponent,
    NewsComponent,
    PlanComponent,
    UserComponent,
    ManageSubscriptionComponent,
    ManagePaymentComponent
  ]
})

export class AdminPanelComponentsModule {}
