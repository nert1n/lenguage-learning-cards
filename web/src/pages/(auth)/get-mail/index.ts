import { lazy } from "react";

export const GetMailPage = lazy(() =>
	import("./ui/get-mail").then(module => ({ default: module.GetMail }))
);
