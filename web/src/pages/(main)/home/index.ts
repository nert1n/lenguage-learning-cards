import { lazy } from "react";

export const HomePage = lazy(() =>
	import("./ui/home").then(module => ({ default: module.Home }))
);
