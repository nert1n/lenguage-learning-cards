import { lazy } from "react";

export const DictionaryPage = lazy(() =>
	import("./ui/dictionary").then(module => ({ default: module.Dictionary }))
);
