import { lazy } from "react";

export const ActivateAccountPage = lazy(() =>
	import("./ui/activate-account").then(module => ({
		default: module.ActivateAccount,
	}))
);
