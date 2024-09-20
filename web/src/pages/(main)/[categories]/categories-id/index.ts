import { lazy } from "react";

export const CategoriesIdPage = lazy(() =>
	import("./ui/categories-id").then(module => ({
		default: module.CategoriesId,
	}))
);
