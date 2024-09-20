import { lazy } from "react";

export const PhrasesPage = lazy(() =>
	import("./ui/phrases").then(module => ({ default: module.Phrases }))
);
