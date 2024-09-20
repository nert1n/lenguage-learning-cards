import { lazy } from "react";

export const TrainingIdPage = lazy(() =>
	import("./ui/training-id").then(module => ({
		default: module.TrainingId,
	}))
);
