import { AuthLayout } from "@app/layout";
import { ActivateAccountPage } from "@pages/(auth)/activate-account";
import { ChangePasswordPage } from "@pages/(auth)/change-password";
import { GetMailPage } from "@pages/(auth)/get-mail";
import { ResetAccountPage } from "@pages/(auth)/reset-account";
import { SignInPage } from "@pages/(auth)/sign-in";
import { SignUpPage } from "@pages/(auth)/sign-up";

export const authRoutes = [
	{
		path: "auth",
		element: <AuthLayout />,
		children: [
			{
				path: "sign-in",
				element: <SignInPage />,
			},
			{
				path: "sign-up",
				element: <SignUpPage />,
			},
			{
				path: "reset-account",
				element: <ResetAccountPage />,
			},
			{
				path: "get-mail",
				element: <GetMailPage />,
			},
			{
				path: "change-password",
				element: <ChangePasswordPage />,
			},
			{
				path: "activate-account",
				element: <ActivateAccountPage />,
			},
		],
	},
];
