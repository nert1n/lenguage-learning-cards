import { lazy } from "react";

export const EditInCategoryIdPage = lazy(() =>
	import("./ui/edit-in-category-id").then(module => ({
		default: module.EditInCategoryId,
	}))
);
