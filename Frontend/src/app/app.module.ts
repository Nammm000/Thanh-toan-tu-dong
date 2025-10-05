import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './shared/material-module';
import { HomeComponent } from './home/home.component';
// import { BestSellerComponent } from './best-seller/best-seller.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SharedModule } from './shared/shared.module';
import { FullComponent } from './layouts/full/full.component';
import { AppHeaderComponent } from './layouts/full/header/header.component';
import { AppSidebarComponent } from './layouts/full/sidebar/sidebar.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgxUiLoaderConfig, NgxUiLoaderModule, SPINNER } from 'ngx-ui-loader';

import { SignupComponent } from './login-signup-form/signup/signup.component';
import { ForgotPasswordComponent } from './login-signup-form/forgot-password/forgot-password.component';
import { LoginComponent } from './login-signup-form/login/login.component';

import { ManageUserComponent } from './admin-panel/manage-user/manage-user.component';

import { TokenInterceptorInterceptor } from './service/token-interceptor.interceptor';
import { ConfirmationComponent } from './material-component/dialog/confirmation/confirmation.component';
import { ChangePasswordComponent } from './material-component/dialog/change-password/change-password.component';
import { ManageCategoryComponent } from './material-component/manage-category/manage-category.component';
import { CategoryComponent } from './material-component/dialog/category/category.component';
import { ManageProductComponent } from './material-component/manage-product/manage-product.component';
// import { ProductComponent } from './material-component/dialog/product/product.component';
// import { CustomerComponent } from './material-component/dialog/customer/customer.component';
// import { WarehouseComponent } from './material-component/dialog/warehouse/warehouse.component';
// import { ProviderComponent } from './material-component/dialog/provider/provider.component';
import { LocationComponent } from './material-component/dialog/location/location.component';
import { ManageOrderComponent } from './material-component/manage-order/manage-order.component';
import { ManageCustomerComponent } from './material-component/manage-customer/manage-customer.component';
import { ManageWarehouseComponent } from './material-component/manage-warehouse/manage-warehouse.component';
import { ManageProviderComponent } from './material-component/manage-provider/manage-provider.component';
import { ManageLocationComponent } from './material-component/manage-location/manage-location.component';
import { ViewBillComponent } from './material-component/view-bill/view-bill.component';

import { FooterComponent } from './layouts/full/footer/footer.component';
import { ItemsComponent } from './layouts/full/header/items/items.component';
import { AboutComponent } from './unauthenticated/about/about.component';
import { ContactComponent } from './unauthenticated/contact/contact.component';
import { SupportComponent } from './unauthenticated/support/support.component';
import { PricingComponent } from './unauthenticated/pricing/pricing.component';
import { LeftSidebarComponent } from './layouts/full/left-sidebar/left-sidebar.component';
import { NewsComponent } from './unauthenticated/news/news.component';
import { NgxEditorModule } from "ngx-editor";
import { ItemComponent } from './unauthenticated/news/item/item.component';
import { NewsCardComponent } from './unauthenticated/news/news-card/news-card.component';
import { HeadNewsCardComponent } from './unauthenticated/news/head-news-card/head-news-card.component';

const ngxUiloaderConfig: NgxUiLoaderConfig = {
  text: "Loading...",
  textColor: "#FFFFFF",
  textPosition: "center-center",
  bgsColor: "#7b1fa2",
  fgsColor: "7b1fa2",
  fgsType: SPINNER.squareJellyBox,
  fgsSize: 100,
  hasProgressBar: false

}

@NgModule({
  declarations: [	
    AppComponent,
    HomeComponent,
    // BestSellerComponent,
    FullComponent,
    AppHeaderComponent,
    AppSidebarComponent,
    SignupComponent,
    ForgotPasswordComponent,
    LoginComponent,
    ConfirmationComponent,
    ChangePasswordComponent,
    ManageCategoryComponent,
    CategoryComponent,
    ManageProductComponent,
    ManageOrderComponent,
    ManageCustomerComponent,
    ManageWarehouseComponent,
    ManageProviderComponent,
    ManageLocationComponent,
    ViewBillComponent,
    ManageUserComponent,
    // ProductComponent, 
    // CustomerComponent, 
    // WarehouseComponent,
    // ProviderComponent, 
    LocationComponent, FooterComponent, ItemsComponent, AboutComponent, ContactComponent, SupportComponent, PricingComponent, LeftSidebarComponent, NewsComponent, ItemComponent, NewsCardComponent, HeadNewsCardComponent,
   ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
    SharedModule,
    HttpClientModule,
    NgxEditorModule,
    NgxUiLoaderModule.forRoot(ngxUiloaderConfig)
  ],
  providers: [HttpClientModule, {provide:HTTP_INTERCEPTORS, useClass:TokenInterceptorInterceptor, multi:true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
