import { lazy } from "react";

export const ResetAccountPage = lazy(() =>
	import("./ui/reset-account").then(module => ({
		default: module.ResetAccount,
	}))
);
