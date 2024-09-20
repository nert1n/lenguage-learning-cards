import { lazy } from "react";

export const StartPage = lazy(() =>
	import("./ui/start").then(module => ({ default: module.Start }))
);
