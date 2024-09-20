import { lazy } from "react";

export const CreateCategoryPage = lazy(() =>
	import("./ui/create-category").then(module => ({
		default: module.CreateCategory,
	}))
);
