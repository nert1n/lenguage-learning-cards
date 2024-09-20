import { lazy } from "react";

export const EditCategoryIdPage = lazy(() =>
	import("./ui/edit-category-id.tsx").then(module => ({
		default: module.EditCategoryId,
	}))
);
