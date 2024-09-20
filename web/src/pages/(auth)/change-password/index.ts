import { lazy } from "react";

export const ChangePasswordPage = lazy(() =>
	import("./ui/change-password").then(module => ({
		default: module.ChangePassword,
	}))
);
