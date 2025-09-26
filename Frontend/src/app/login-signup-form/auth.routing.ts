import { Routes } from '@angular/router';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { RouteGuardService } from '../service/route-guard.service';
import { SignupComponent } from './signup/signup.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { LoginComponent } from './login/login.component';


export const AuthRoutes: Routes = [
    {
        path: 'login',
        component: LoginComponent,
    },
    {
        path: 'signup',
        component: SignupComponent,
    },
    {
        path: 'forgot-password',
        component: ForgotPasswordComponent,
        // canActivate: [RouteGuardService],
        // data: {
        //     expectedRole: ['ADMIN', 'USER']
        // }
    }
];
