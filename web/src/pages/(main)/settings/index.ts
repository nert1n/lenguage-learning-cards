import { lazy } from "react";

export const SettingsPage = lazy(() =>
	import("./ui/settings").then(module => ({ default: module.Settings }))
);
