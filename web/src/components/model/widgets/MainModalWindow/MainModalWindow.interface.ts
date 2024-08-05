import { ReactNode } from 'react';

import { ComponentTheme } from '@/config/component-theme.type';

export interface IMainModalWindow {
	children: ReactNode;
	theme?: ComponentTheme;
	className?: string;
	onClose: () => void;
}
