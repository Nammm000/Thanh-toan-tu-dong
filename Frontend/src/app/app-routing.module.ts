import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { FullComponent } from './layouts/full/full.component';
import { RouteGuardService } from './service/route-guard.service';

import { AboutComponent } from './unauthenticated/about/about.component';
import { ContactComponent } from './unauthenticated/contact/contact.component';
import { SupportComponent } from './unauthenticated/support/support.component';
import { PricingComponent } from './unauthenticated/pricing/pricing.component';
import { NewsComponent } from './unauthenticated/news/news.component';

import { AdminComponent } from './dashboard/admin/admin.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'support', component: SupportComponent },
  { path: 'pricing', component: PricingComponent },
  { path: 'news', component: NewsComponent },
  // { path: 'auth/login', component: LoginComponent },
  // { path: 'auth/signup', component: SignupComponent },
  // { path: 'auth/forgot-password', component: ForgotPasswordComponent },
  {
    path: 'auth',
    loadChildren: () =>
      import('./login-signup-form/auth.module').then(
        (m) => m.AuthComponentsModule
      ),
  },
  { 
    path: 'dashboard', component: AdminComponent, 
    canActivate: [RouteGuardService], 
    data: {
          expectedRole: ['ADMIN', 'USER'],
        },
  },
  {
    path: 'admin',
    component: FullComponent,
    loadChildren: () =>
      import('./admin-panel/admin-panel.module').then(
        (m) => m.AdminPanelComponentsModule
      ),
  },
  {
    path: 'cafe',
    component: FullComponent,
    children: [
      {
        path: '',
        redirectTo: '/cafe/dashboard',
        pathMatch: 'full',
      },
      {
        path: '',
        loadChildren: () =>
          import('./material-component/material.module').then(
            (m) => m.MaterialComponentsModule
          ),
        canActivate: [RouteGuardService],
        data: {
          expectedRole: ['ADMIN', 'USER'],
        },
      },
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./dashboard/dashboard.module').then((m) => m.DashboardModule),
        canActivate: [RouteGuardService],
        data: {
          expectedRole: ['ADMIN', 'USER'],
        },
      },
    ],
  },
  { path: '**', component: HomeComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
